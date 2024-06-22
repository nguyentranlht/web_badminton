package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.repository.IBadmintonRepository;
import com.example.webBadminton.repository.ICourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BadmintonService {
    @Autowired
    private IBadmintonRepository badmintonRepository;

    @Autowired
    private ICourtRepository courtRepository;

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

}
