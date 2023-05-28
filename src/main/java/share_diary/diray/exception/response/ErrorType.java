package share_diary.diray.exception.response;

import share_diary.diray.exception.BaseException;
import share_diary.diray.exception.jwt.AccessTokenRenewException;
import share_diary.diray.exception.jwt.TokenExpiredException;
import share_diary.diray.exception.jwt.TokenIsNotValidException;
import share_diary.diray.exception.member.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ErrorType {

    A001("A001","토큰이 유효하지 않습니다.", TokenIsNotValidException.class),
    A002("A002","토큰이 만료되었습니다.", TokenExpiredException.class),
    A003("A003","ACCESS TOKEN 이 만료되었습니다.", AccessTokenRenewException.class),

    M001("M001","회원을 찾을 수 없습니다.", MemberNotFoundException.class),
    M002("M002","아이디 혹은 비밀번호가 올바르지 않습니다.",MemberIdOrPasswordErrorException.class),
    M003("M003","이미 등록된 아이디가 존재합니다.",ValidationMemberIdException.class),
    M004("M004","이미 등록된 이메일이 존재합니다.", ValidationMemberEmailException.class),
    M005("M005","비밀번호가 일치하지 않습니다.", PasswordNotCoincide.class),
    M006("M006","수정하려는 비밀번호가 일치하지 않습니다.", UpdatePasswordNotCoincide.class),
    ;

    private final String code;
    private final String message;
    private final Class< ? extends BaseException> classType;

    private static final Map<Class<? extends BaseException>,ErrorType> codeMap = new HashMap<>();

    static {
        Arrays.stream(values())
                .forEach(errorType -> codeMap.put(errorType.classType,errorType));
    }

    public static ErrorType of(Class<? extends BaseException> errorType){
        return codeMap.get(errorType);
    }

    ErrorType(String code, String message, Class<? extends BaseException> classType) {
        this.code = code;
        this.message = message;
        this.classType = classType;
    }
}
