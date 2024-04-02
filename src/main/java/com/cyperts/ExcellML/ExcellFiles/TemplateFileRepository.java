package com.cyperts.ExcellML.ExcellFiles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateFileRepository extends JpaRepository<TemplateFile, Long> {
	List<TemplateFile> getDataByUserId(long userId);

	void deleteFileDataByUserId(long userId);

}
