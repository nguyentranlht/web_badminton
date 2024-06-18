package com.example.webBadminton.service;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.model.Court;
import com.example.webBadminton.repository.ICourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourtService {
    @Autowired
    private final ICourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public Optional<Court> getCourtById(Long id) {
        return courtRepository.findById(id);
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

    public void updateCourt(@NotNull Court court) {
        Court existingCourt = courtRepository.findById(court.getId())
                .orElseThrow(() -> new IllegalStateException("Court with ID " + court.getId() + " does not exist."));

        existingCourt.setCourtName(court.getCourtName());
        /*existingCourt.setPrice(court.getPrice());
        existingCourt.setDescription(court.getDescription());
        existingCourt.setCategory(court.getCategory());*/
        courtRepository.save(existingCourt);
    }
}
