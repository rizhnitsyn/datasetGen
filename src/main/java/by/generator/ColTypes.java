package by.generator;

import by.generator.utils.GeneratorPattern;

public enum ColTypes implements DefaultValues {
      VARCHAR{
        @Override
        public Integer getDefaultLength() {
            return 30;
        }
    }, NUMBER{
        @Override
        public Integer getDefaultLength() {
            return 6;
        }
    }, INTEGER{
        @Override
        public Integer getDefaultLength() {
            return 6;
        }
    }, BOOLEAN{
        @Override
        public Integer getDefaultLength() {
            return null;
        }
    }, DATE{
        @Override
        public Integer getDefaultLength() {
            return null;
        }
    }
}
