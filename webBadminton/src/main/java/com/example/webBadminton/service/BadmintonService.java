package com.example.webBadminton.service;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.repository.IBadmintonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadmintonService {
    @Autowired
    private IBadmintonRepository badmintonRepository;

    public List<Badminton> getAllBadmintons() {
        return badmintonRepository.findAll();
    }

    public Badminton getBadmintonById(Long id) {
        return badmintonRepository.findById(id).orElse(null);
    }

    public void addBadminton(Badminton badminton) {
        badmintonRepository.save(badminton);
    }

    public void deleteBadminton(Long id) {
        badmintonRepository.deleteById(id);
    }

    public void saveBadminton(Badminton badminton) {
        badmintonRepository.save(badminton);
    }
}
