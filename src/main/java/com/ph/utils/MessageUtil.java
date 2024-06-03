package com.ph.utils;

import com.ph.config.I18nConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getMessage(String message) {
        return I18nConfig.messageSource().getMessage(message, null, "Error: Message not found", LocaleContextHolder.getLocale());
    }

    public String getMessage(String message, Object[] args) {
        return getMessage(message, args, "Error: Message not found", LocaleContextHolder.getLocale());
    }

    public String getMessage(String message, Object[] args, String defaultMessage) {
        return getMessage(message, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    public String getMessage(String message, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(message, args, defaultMessage, locale);
    }


}
