package b.s.wordle.dto;

import b.s.wordle.enums.GuessColor;

public record GuessCharacter (char ch, GuessColor color){
}
