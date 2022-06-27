package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.QuestionAnswer;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayerWithState {

	private SynchronousPlayer player;

	private QuestionAnswer answer;

	private PlayerState state;
	
}
