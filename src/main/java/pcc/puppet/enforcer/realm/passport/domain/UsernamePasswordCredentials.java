package pcc.puppet.enforcer.realm.passport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordCredentials {
private String username;
private String password;
}
