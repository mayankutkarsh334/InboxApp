package io.javabrains.inbox;

import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;

@SpringBootApplication
@RestController
public class InboxApp {

	@Autowired
	FolderRepository folderRepository;

	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("mayankutkarsh334", "inbox", "blue"));
		folderRepository.save(new Folder("mayankutkarsh334", "Sent", "green"));
		folderRepository.save(new Folder("mayankutkarsh334", "Important", "yellow"));
		// for (int i = 0; i < 10; i++) {
		// emailService.sendEmail("koushikkothagal", "koushikkothagal", "Test " + i,
		// "Body " + i);
		// }
	}

}
