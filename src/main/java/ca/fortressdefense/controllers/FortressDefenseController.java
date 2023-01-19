package ca.fortressdefense.controllers;

import ca.fortressdefense.api.ApiBoardWrapper;
import ca.fortressdefense.api.ApiGameWrapper;
import ca.fortressdefense.api.ApiLocationWrapper;
import ca.fortressdefense.model.CellLocation;
import ca.fortressdefense.model.Game;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class FortressDefenseController {
    private final List<Game> games = new ArrayList<>();
    private final List<Boolean> cheats = new ArrayList<>();

    @GetMapping("/hello")
    public String getHelloMessage() {
        return "Hello world";
    }

    @GetMapping("/api/about")
    public String getAbout() {
        return "Eric Wong";
    }

    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper createNewGame() {
        Game game = new Game(5);
        games.add(game);
        cheats.add(false);
        return new ApiGameWrapper(games.indexOf(game), game);
    }

    @GetMapping("/api/games")
    public List<ApiGameWrapper> getAllGames() {
        List<ApiGameWrapper> gameWrappers = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            gameWrappers.add(new ApiGameWrapper(i, games.get(i)));
        }
        return gameWrappers;
    }

    @GetMapping("/api/games/{id}")
    public ApiGameWrapper getOneGame(@PathVariable("id") int gameNumber) {
        try {
            return new ApiGameWrapper(gameNumber, games.get(gameNumber));
        } catch (IndexOutOfBoundsException e){
            throw new NotFound("ERROR: Game does not exist.");
        }

    }

    @GetMapping("/api/games/{id}/board")
    public ApiBoardWrapper getOneGameBoard(@PathVariable("id") int gameNumber) {
        try {
            return new ApiBoardWrapper(games.get(gameNumber), cheats.get(gameNumber));
        } catch (IndexOutOfBoundsException e) {
            throw new NotFound("ERROR: Game does not exist.");
        }
    }

    @PostMapping("/api/games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeMove(@RequestBody ApiLocationWrapper location, @PathVariable("id") int gameNumber) {
        Game game;
        try {
            game = games.get(gameNumber);

        } catch (IndexOutOfBoundsException e) {
            throw new NotFound("ERROR: Game does not exist.");
        }

        if (location.col >= 0 && location.col <= 9 && location.row >= 0 && location.row <= 9) {
            game.recordPlayerShot(new CellLocation(location.row, location.col));
            game.fireTanks();
        } else {
            throw new BadRequest("ERROR: Location is invalid.");
        }

    }

    @PostMapping("/api/games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void startCheats(@RequestBody String string, @PathVariable("id") int gameNumber) throws IllegalAccessException {
        try {
            games.get(gameNumber);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFound("ERROR: Game does not exist.");
        }

        if (Objects.equals(string, "SHOW_ALL")) {
            cheats.set(gameNumber, true);
        } else {
            throw new BadRequest("ERROR: Not valid cheat String.");
        }
    }

    // Support returning errors to client
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public  static class BadRequest extends RuntimeException {
        public BadRequest() {}
        public BadRequest(String str) {
            super(str);
        }
    }

    // Support returning errors to client
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public  static class NotFound extends RuntimeException {
        public NotFound() {}
        public NotFound(String str) {
            super(str);
        }
    }
}
