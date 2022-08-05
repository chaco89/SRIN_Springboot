package com.example.springboot.config;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Payload;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@Configuration
public class SftpConfig {

	@Value("${sftp.host:localhost}")
    private String sftpHost;

    @Value("${sftp.port:22}")
    private int sftpPort;

    @Value("${sftp.user:@null}")
    private String sftpUser;

    @Value("${sftp.privateKey:@null}")
    private Resource sftpPrivateKey;

    @Value("${sftp.privateKeyPassphrase:@null}")
    private String sftpPrivateKeyPassphrase;

    @Value("${sftp.password:@null}")
    private String sftpPasword;

    @Value("${sftp.remote.directory:/}")
    private String sftpRemoteDirectory;

    @Bean
    public SessionFactory<LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(sftpHost);
        factory.setPort(sftpPort);
        factory.setUser(sftpUser);
        if (sftpPrivateKey != null) {
            factory.setPrivateKey(sftpPrivateKey);
            factory.setPrivateKeyPassphrase(sftpPrivateKeyPassphrase);
        } else {
            factory.setPassword(sftpPasword);
        }
        factory.setAllowUnknownKeys(true);
        factory.setTimeout(1000);

        return new CachingSessionFactory<LsEntry>(factory);

    }

    @Bean
    @ServiceActivator(inputChannel = "uploadSftpChannel")
    public MessageHandler upload() {
        SftpMessageHandler handler = new SftpMessageHandler(new SftpRemoteFileTemplate(sftpSessionFactory()), FileExistsMode.REPLACE);
        handler.setRemoteDirectoryExpression(new LiteralExpression(sftpRemoteDirectory) );
        handler.setLoggingEnabled(true);
        handler.setFileNameGenerator(new FileNameGenerator() {
            @Override
            public String generateFileName(Message<?> message) {
                if (message.getPayload() instanceof File) {
                    return ((File) message.getPayload()).getName();
                } else {
                    throw new IllegalArgumentException("File expected as payload.");
                }
            }
        });
        return handler;
    }


    @Bean
    @ServiceActivator(inputChannel = "listSftpChannel")
    public MessageHandler list() {
        return new SftpOutboundGateway(sftpSessionFactory(), "ls", "'"+sftpRemoteDirectory+"'");
    }

    @Bean
    @ServiceActivator(inputChannel = "deleteSftpChannel")
    public MessageHandler delete() {
		SftpOutboundGateway sftpOutbound = new SftpOutboundGateway(sftpSessionFactory(), "rm", "payload");
		return sftpOutbound;
    }


    @MessagingGateway
    public interface UploadGateway {

        @Gateway(requestChannel = "uploadSftpChannel")
        void upload(File file);

        @Gateway(requestChannel = "deleteSftpChannel")
        void delete(String filename);

        @Gateway(requestChannel = "listSftpChannel")
        @Payload("new java.util.Date()")
        List<SftpFileInfo> list();
    }


}
