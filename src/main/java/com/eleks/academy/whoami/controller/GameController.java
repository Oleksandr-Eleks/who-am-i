package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.enums.QuestionAnswer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.Message;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.PlayerDetails;
import com.eleks.academy.whoami.model.response.TurnDetails;
import com.eleks.academy.whoami.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.eleks.academy.whoami.utils.StringUtils.Headers.PLAYER;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public List<PersistentGame> findAvailableGames(@RequestHeader(PLAYER) String player) {
        return this.gameService.findAvailableGames();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameDetails createGame(@RequestHeader(PLAYER) String player,
                                  @Valid @RequestBody NewGameRequest gameRequest) {
        return this.gameService.createGame(player, gameRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDetails> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(this.gameService.findGameById(id));
    }

    @PostMapping("/{id}/players")
    public PlayerDetails enrollToGame(@PathVariable("id") String id,
                                      @RequestHeader(PLAYER) String player) {
        return this.gameService.enrollToGame(id, player);
    }

    @PostMapping("/{id}/characters")
    @ResponseStatus(HttpStatus.OK)
    public void suggestCharacter(@PathVariable("id") String id,
                                 @RequestHeader(PLAYER) String player,
                                 @Valid @RequestBody CharacterSuggestion suggestion) {
        this.gameService.suggestCharacter(id, player, suggestion);
    }

    @GetMapping("/{id}/turn")
    public ResponseEntity<TurnDetails> findTurnInfo(@PathVariable("id") String id,
                                                    @RequestHeader(PLAYER) String player) {
        return this.gameService.findTurnInfo(id, player)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<GameDetails> startGame(@PathVariable("id") String id,
                                                 @RequestHeader(PLAYER) String player) {
        return this.gameService.startGame(id, player)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/questions")
    public void askQuestion(@PathVariable("id") String id,
                            @RequestHeader(PLAYER) String player, @RequestBody Message message) {
        this.gameService.askQuestion(id, player, message.getMessage());
    }

    @PostMapping("/{id}/guess")
    public void submitGuess(@PathVariable("id") String id,
                            @RequestHeader(PLAYER) String player, @RequestBody Message message) {
        this.gameService.submitGuess(id, player, message);
    }

    @PostMapping("/{id}/answer")
    public void answerQuestion(@PathVariable("id") String id,
                               @RequestHeader(PLAYER) String player, @RequestParam QuestionAnswer answer) {
        this.gameService.answerQuestion(id, player, answer);

    }

    @PostMapping("/{id}/guess/answer")
    public void answerGuessingQuestion(@PathVariable("id") String id,
                                       @RequestHeader(PLAYER) String player, @RequestParam QuestionAnswer answer) {
        this.gameService.answerGuessingQuestion(id, player, answer);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<String> getGameHistory(@PathVariable("id") String id) {
        return ResponseEntity.ok(this.gameService.gameHistory(id));
    }

    @DeleteMapping("/{id}/leave")
    public void leaveGame(@PathVariable("id") String id,
                          @RequestHeader(PLAYER) String player) {
        this.gameService.leaveGame(id, player);
    }

    @GetMapping("/showPlayers")
    public int showPlayers() {
        return this.gameService.getAllPlayers();
    }

}
