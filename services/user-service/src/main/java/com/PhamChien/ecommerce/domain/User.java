    package com.PhamChien.ecommerce.domain;

    import com.PhamChien.ecommerce.util.Gender;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDate;
    import java.util.Set;

    @Entity
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "user")
    public class    User extends AbstractEntity<Long>{
        @Column(name = "full_name")
        private String fullName;

        @Column(name = "gender")
        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Column(name = "avatar_url")
        private String avatarUrl;

        @Column(name = "phone")
        private String phone;

        @Column(name = "dob")
        private LocalDate dob;

        @Column(name = "credential_id")
        private String credentialId;

        @JsonIgnore
        @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Profile> profiles;
    }
