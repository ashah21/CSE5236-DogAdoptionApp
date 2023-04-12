package com.example.dog_test.model;

public class Dog {

    public String dogId;
    public String name;

    public String breed;

    public int age;

    public int weight;

    public boolean isVaccinated;

    public boolean isSterilized;

    public String bio;

    public String imageUrl;

    public String ownerId;

    public boolean isAdopted;

    public Dog() {

    }

    public String getId() {
        return dogId;
    }
    public void setId(String id) {
        dogId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getBreed() { return breed; }

    public void setBreed(String newBreed) { breed = newBreed; }

    public int getAge() {
        return age;
    }

    public void setAge(int newAge) {
        age = newAge;
    }

    public int getWeight() { return weight; }

    public void setWeight(int newWeight) { weight = newWeight; }

    public boolean getIsVaccinated() { return isVaccinated; }

    public void setIsVaccinated(boolean vaccinated) { isVaccinated = vaccinated; }

    public boolean getIsSterilized() { return isSterilized; }

    public void setIsSterilized(boolean sterilized) { isSterilized = sterilized; }

    public String getBio() { return bio; }

    public void setBio(String newBio) { bio = newBio; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String newImageUrl) { imageUrl = newImageUrl; }

    public String getOwnerId() { return ownerId; }

    public void setOwnerId(String newOwner) { ownerId = newOwner; }

    public boolean getIsAdopted() { return isAdopted; }

    public void setIsAdopted(boolean adopted) { isAdopted = adopted; }

}
