package com.mall.controller;

import com.mall.service.BaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agreement")
public class AgreementController {
    @Autowired
    private BaseAgreementService agreementService;


}
