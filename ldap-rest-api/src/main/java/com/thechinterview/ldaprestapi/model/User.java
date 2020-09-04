package com.thechinterview.ldaprestapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.validation.constraints.NotBlank;
import java.util.Formatter;

@AllArgsConstructor
@Getter
@Setter
@Entry(
        objectClasses = { "inetOrgPerson", "organizationalPerson", "person", "top" },
        base = "ou=Users,dc=techinterview,dc=com")
public class User implements Comparable<User> {

    public User(){}

    @Id
    @JsonIgnore
    private Name id;

    @ApiModelProperty(value = "unique id")
    @Attribute(name="uid")
    @NotBlank
    private String uid;

    @ApiModelProperty(value = "First name")
    @Attribute(name="cn")
    @NotBlank
    private String cn;

    @ApiModelProperty(value = "Last name")
    @Attribute(name="sn")
    @NotBlank
    private String sn;

    @Override
    public int compareTo(User o) {
        boolean allAttributesEqual = getUid().equals(o.getUid())
                && getCn().equals(o.getCn())
                && getSn().equals(o.getSn());
        return  allAttributesEqual ? 0 : 1;
    }

    public String toString() {
        return new Formatter().format("{\"cn\": \"%s\", \"sn\": \"%s\", \"uid\": \"%s\"}",
                cn, sn, uid).toString();
    }
}
