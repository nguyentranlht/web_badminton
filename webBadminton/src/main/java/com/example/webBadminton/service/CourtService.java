package com.example.webBadminton.service;

import com.example.webBadminton.model.Court;
import com.example.webBadminton.repository.ICourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtService {
    @Autowired
    private ICourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public List<Court> getAllCourtsByIdBadminton(Long id) {
        return courtRepository.findByBadmintonId(id);
    }

    public Court getCourtById(Long id) {
        return courtRepository.findById(id).orElse(null);
    }

    public void addCourt(Court court) {
        courtRepository.save(court);
    }

    public void deleteCourt(Long id) {
        courtRepository.deleteById(id);
    }

    public void saveCourt(Court court) {
        courtRepository.save(court);
    }
}
