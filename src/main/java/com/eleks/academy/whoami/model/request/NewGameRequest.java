package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewGameRequest {

	//TODO: Retrieve from config
	@Min(2)
	@Max(6)
	@NotNull(message = "maxPlayers must not be null")
	private Integer maxPlayers;
}
