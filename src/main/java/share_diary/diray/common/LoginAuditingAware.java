package share_diary.diray.common;

import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import share_diary.diray.auth.AuthService;
import share_diary.diray.common.utils.Utils;
import share_diary.diray.member.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class LoginAuditingAware implements AuditorAware<String> {

    private final AuthService authService;
    private final MemberService memberService;

    public LoginAuditingAware(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(Utils.isNullAndBlank(token)){
            return Optional.empty();
        }
        Long id = authService.extractIdByToken(token);
        return Optional.ofNullable(memberService.findMemberById(id).getLoginId());
    }
}
