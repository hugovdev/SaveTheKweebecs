package me.hugo.savethekweebecs.utils.rainbow;

public class HomogeneousRainbowException extends RainbowException {

    private static final long serialVersionUID = -3883632693158928681L;

    public String getMessage() {
        return "Rainbow must have two or more colours.";
    }

}
