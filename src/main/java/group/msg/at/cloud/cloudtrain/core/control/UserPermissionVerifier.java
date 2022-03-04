package group.msg.at.cloud.cloudtrain.core.control;

import group.msg.at.cloud.cloudtrain.adapter.rest.grantedpermissions.GrantedPermission;
import group.msg.at.cloud.cloudtrain.adapter.rest.grantedpermissions.GrantedPermissionsClient;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.security.Principal;
import java.util.List;

@Dependent
public class UserPermissionVerifier {

    @Inject
    @RestClient
    GrantedPermissionsClient client;

    @Inject
    Principal currentUser;

    public boolean hasPermission(String permission) {

        List<GrantedPermission> permissions = client.getGrantedPermissionsByCurrentUser();
        for (GrantedPermission current : permissions) {
            if (current.getPermission().equals(permission)) {
                return true;
            }
        }

        return false;
    }

    public void requirePermission(String permission) {
        if (!hasPermission(permission)) {
            throw new IllegalStateException(String.format("missing required permission [%s] for user [%s]", permission, currentUser.getName()));
        }
    }
}
