package oreschnix.cryptosatistics.util;

/**
 * Created by miha.novak on 25/02/2018.
 */

/**
 * Dual condition checker class.
 */
public class PairCheck {

    private boolean firstCheck;
    private boolean secondCheck;

    public PairCheck() {
        this.firstCheck = false;
        this.secondCheck = false;
    }

    public boolean setFirstAndCheck(boolean firstCheck) {
        this.firstCheck = firstCheck;
        return firstCheck && secondCheck;
    }

    public boolean setSecondAndCheck(boolean secondCheck) {
        this.secondCheck = secondCheck;
        return firstCheck && secondCheck;
    }
}
