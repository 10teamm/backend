package com.swyp.catsgotogedog.pet.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PetNameValidator implements ConstraintValidator<ValidPetName, String> {

    // 허용된 특수문자: 쉼표(,), 마침표(.), 작은따옴표(')
    private static final Pattern ALLOWED_SPECIAL_CHARS = Pattern.compile("[,.'_]");

    // 허용되지 않은 문자 체크 (숫자 추가, 한글 자음/모음은 3번에서 별도 처리)
    private static final Pattern FORBIDDEN_CHARS = Pattern.compile("[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9,.'_]");

    // 특수문자 연속 사용 체크
    private static final Pattern CONSECUTIVE_SPECIAL_CHARS = Pattern.compile("[,.'_]{2,}");

    // 한글 불완전한 단어 체크 (자음만 또는 모음만)
    private static final Pattern INCOMPLETE_KOREAN = Pattern.compile(".*[ㄱ-ㅎㅏ-ㅣ].*");

    // 영어 자음 또는 모음 4회 이상 연속
    private static final Pattern CONSECUTIVE_CONSONANTS = Pattern.compile(".*[bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ]{4,}.*");
    private static final Pattern CONSECUTIVE_VOWELS = Pattern.compile(".*[aeiouAEIOU]{4,}.*");

    @Override
    public void initialize(ValidPetName constraintAnnotation) {
        // 초기화 로직 (필요시)
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.trim().isEmpty()) {
            return true; // @NotBlank에서 처리
        }

        String trimmedName = name.trim();

        // 1. 허용되지 않은 특수문자 체크
        if (FORBIDDEN_CHARS.matcher(trimmedName).find()) {
            setCustomMessage(context, "한글, 영어, 숫자, 쉼표(,), 마침표(.), 작은따옴표(')만 사용할 수 있습니다.");
            return false;
        }

        // 2. 특수문자 연속 사용 체크
        if (CONSECUTIVE_SPECIAL_CHARS.matcher(trimmedName).find()) {
            setCustomMessage(context, "특수문자는 연속으로 사용할 수 없습니다.");
            return false;
        }

        // 3. 한글 불완전한 단어 체크 (자음만 또는 모음만)
        if (INCOMPLETE_KOREAN.matcher(trimmedName).find()) {
            setCustomMessage(context, "올바른 단어를 입력해주세요.");
            return false;
        }

        // 4. 영어 자음 또는 모음 4회 이상 연속 체크
        if (CONSECUTIVE_CONSONANTS.matcher(trimmedName).find() ||
            CONSECUTIVE_VOWELS.matcher(trimmedName).find()) {
            setCustomMessage(context, "올바른 단어를 입력해주세요.");
            return false;
        }

        return true;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
    }
}
