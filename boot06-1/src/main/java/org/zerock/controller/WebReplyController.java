package org.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.persistence.WebReplyRepository;

@RestController
@RequestMapping("/replies/*")
public class WebReplyController {

	@Autowired
	private WebReplyRepository replyRepo;
}
