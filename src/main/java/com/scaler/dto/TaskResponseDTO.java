package com.scaler.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import com.scaler.entities.NoteEntity;

@NoArgsConstructor
@Getter
@Setter
public class TaskResponseDTO {
	private int id;
	private String title;
	private String description;
	private Date deadline;
	private boolean completed;
	private List<NoteEntity> notes;
}