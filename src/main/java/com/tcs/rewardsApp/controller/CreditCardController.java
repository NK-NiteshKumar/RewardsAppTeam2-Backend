package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.dto.request.CreditCardLinkRequest;
import com.tcs.rewardsApp.dto.response.CreditCardResponse;
import com.tcs.rewardsApp.service.card.CreditCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void linkCard(
            @PathVariable Long customerId,
            @Valid @RequestBody CreditCardLinkRequest request,
            Principal principal
    ) {
        creditCardService.linkCard(
                customerId,
                request,
                principal.getName()
        );
    }

    @GetMapping
    public List<CreditCardResponse> getCardsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "false") boolean includeUnlinked
    ) {
        return creditCardService.getCardsByCustomer(
                customerId,
                includeUnlinked
        );
    }

    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkCard(
            @PathVariable Long customerId,
            @PathVariable Long cardId,
            Principal principal
    ) {
        creditCardService.unlinkCard(
                customerId,
                cardId,
                principal.getName()
        );
    }

    @PutMapping("/{cardId}/relink")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void relinkCard(
            @PathVariable Long customerId,
            @PathVariable Long cardId,
            Principal principal
    ) {
        creditCardService.relinkCard(
                customerId,
                cardId,
                principal.getName()
        );
    }

}
