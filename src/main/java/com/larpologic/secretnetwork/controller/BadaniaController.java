package com.larpologic.secretnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/badania")
public class BadaniaController {

    @GetMapping
    public String getBadaniaPage() {
        return "badania/index";
    }

    @GetMapping("/archivum")
    public String getArchivumPage() {
        return "badania/archivum";
    }

    @GetMapping("/archiwum")
    public String getArchiwumPage() {
        return "badania/archiwum";
    }

    @GetMapping("/soplówka")
    public String getSoplowkaPage() {
        return "badania/soplówka";
    }

    @GetMapping("/reishi")
    public String getReishiPage() {
        return "badania/reishi";
    }

    @GetMapping("/psylocybina")
    public String getPsylocybinaPage() {
        return "badania/psylocybina";
    }
}