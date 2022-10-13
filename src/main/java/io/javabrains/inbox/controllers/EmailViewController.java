package io.javabrains.inbox.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FolderService;

@Controller
public class EmailViewController {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailRepository emailRepository;

    @GetMapping(value = "/emails/{id}")
    public String emailView(
            @PathVariable UUID id,
            @AuthenticationPrincipal OAuth2User principal,
            Model model) {
        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }

        // Fetch folders
        String userId = principal.getAttribute("login");
        System.out.println(userId);
        List<Folder> userFolders = folderRepository.findAllById(userId);
        System.out.println(userFolders);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Optional<Email> optionalEmail = emailRepository.findById(id);
        if (!optionalEmail.isPresent()) {
            return "inbox-page";
        }
        model.addAttribute("email", optionalEmail.get());

        return "email-page";
    }
}
