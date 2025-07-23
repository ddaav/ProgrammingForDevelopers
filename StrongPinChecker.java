public class StrongPinChecker {

    public static int strongPinChecker(String pin) {
        int length = pin.length();

        // Check for missing character types
        boolean hasLower = false, hasUpper = false, hasDigit = false;
        for (char ch : pin.toCharArray()) {
            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
        }

        int missingTypes = 0;
        if (!hasLower) missingTypes++;
        if (!hasUpper) missingTypes++;
        if (!hasDigit) missingTypes++;

        // Count consecutive repeating characters (e.g., "aaa", "111")
        int replace = 0;
        int i = 2; // Start from 3rd character to check previous two
        while (i < length) {
            if (pin.charAt(i) == pin.charAt(i - 1) && pin.charAt(i - 1) == pin.charAt(i - 2)) {
                int repeatLen = 2;
                while (i < length && pin.charAt(i) == pin.charAt(i - 1)) {
                    repeatLen++;
                    i++;
                }
                replace += repeatLen / 3;
            } else {
                i++;
            }
        }

        if (length < 6) {
            return Math.max(6 - length, Math.max(missingTypes, replace));
        } else if (length <= 20) {
            return Math.max(missingTypes, replace);
        } else {
            int delete = length - 20;

            // Optimize replacements by deleting from repeating sequences
            int[] mods = new int[3];
            i = 2;
            while (i < length) {
                if (pin.charAt(i) == pin.charAt(i - 1) && pin.charAt(i - 1) == pin.charAt(i - 2)) {
                    int j = i - 2;
                    while (i < length && pin.charAt(i) == pin.charAt(i - 1)) i++;
                    int repeatLen = i - j;
                    mods[repeatLen % 3]++;
                } else {
                    i++;
                }
            }

            int remainingDeletes = delete;
            for (int mod = 0; mod < 3; mod++) {
                while (mods[mod] > 0 && remainingDeletes > mod) {
                    remainingDeletes -= mod + 1;
                    mods[mod]--;
                    replace--;
                }
            }

            replace -= remainingDeletes / 3;

            return delete + Math.max(missingTypes, replace);
        }
    }

    public static void main(String[] args) {
        String pin = "X1!";
        System.out.println("Minimum changes required: " + strongPinChecker(pin)); // Output: 3
    }
}
