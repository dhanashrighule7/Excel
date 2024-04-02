package com.cyperts.ExcellML.MailIntegration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailHistoryReposistory extends JpaRepository<MailHistory, Long> {
	Page<MailHistory> findAll(Pageable pageable);

}
