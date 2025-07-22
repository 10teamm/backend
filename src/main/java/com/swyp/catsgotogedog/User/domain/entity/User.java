package com.swyp.catsgotogedog.User.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.swyp.catsgotogedog.global.BaseTimeEntity;
import com.swyp.catsgotogedog.pet.domain.entity.Pet;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    private String displayName;
    private String email;
    private String provider;     // google / kakao / naver
    private String providerId;
    private String imageFilename;
    private String imageUrl;
    private Boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pet> pets = new ArrayList<>();

    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setUser(this);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setUser(null);
    }

}