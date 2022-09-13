package cc.xfl12345.mybigdata.server.model.generator;


public class RandomCodeGeneratorOptions {
    private int codeLength = 8;

    private CharacterOptions specialCharacterOptions = new CharacterOptions();

    private CharacterOptions integerOption = new CharacterOptions();

    private LetterOptions letterOption = new LetterOptions();

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public CharacterOptions getSpecialCharacterOptions() {
        return specialCharacterOptions;
    }

    public void setSpecialCharacterOptions(CharacterOptions specialCharacterOptions) {
        this.specialCharacterOptions = specialCharacterOptions;
    }

    public CharacterOptions getIntegerOption() {
        return integerOption;
    }

    public void setIntegerOption(CharacterOptions integerOption) {
        this.integerOption = integerOption;
    }

    public LetterOptions getLetterOption() {
        return letterOption;
    }

    public void setLetterOption(LetterOptions letterOption) {
        this.letterOption = letterOption;
    }

    public static final class Builder {
        private int codeLength;

        private CharacterOptions specialCharacterOptions = new CharacterOptions();

        private CharacterOptions integerOption = new CharacterOptions();

        private LetterOptions letterOption = new LetterOptions();

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withCodeLength(int codeLength) {
            this.codeLength = codeLength;
            return this;
        }

        public Builder withSpecialCharacterOptions(CharacterOptions specialCharacterOptions) {
            this.specialCharacterOptions = specialCharacterOptions;
            return this;
        }

        public Builder withIntegerOption(CharacterOptions integerOption) {
            this.integerOption = integerOption;
            return this;
        }

        public Builder withLetterOption(LetterOptions letterOption) {
            this.letterOption = letterOption;
            return this;
        }

        public RandomCodeGeneratorOptions build() {
            RandomCodeGeneratorOptions randomCodeGeneratorOptions = new RandomCodeGeneratorOptions();
            randomCodeGeneratorOptions.setCodeLength(codeLength);
            randomCodeGeneratorOptions.setSpecialCharacterOptions(specialCharacterOptions);
            randomCodeGeneratorOptions.setIntegerOption(integerOption);
            randomCodeGeneratorOptions.setLetterOption(letterOption);
            return randomCodeGeneratorOptions;
        }
    }
}
