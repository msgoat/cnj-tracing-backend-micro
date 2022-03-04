package group.msg.at.cloud.cloudtrain.adapter.rest.grantedpermissions;

public class GrantedPermission {

    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "GrantedPermission{" +
                "permission='" + permission + '\'' +
                '}';
    }
}
