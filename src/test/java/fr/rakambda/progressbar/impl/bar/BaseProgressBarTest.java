package fr.rakambda.progressbar.impl.bar;

import fr.rakambda.progressbar.impl.render.DefaultRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseProgressBarTest<T extends BaseProgressBar> {
    protected abstract T getProgressBar();

    @Test
    void defaultValues(){
        var tested = getProgressBar();

        assertThat(tested.getRenderer()).isInstanceOf(DefaultRenderer.class);
        assertThat(tested.getName()).isNull();
        assertThat(tested.getUnit()).isNull();
        assertThat(tested.getDescription()).isNull();
        assertThat(tested.isHideWhenComplete()).isEqualTo(false);
        assertThat(tested.isRemoveWhenComplete()).isEqualTo(false);
        assertThat(tested.isShowPercentage()).isEqualTo(false);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"value"})
    void nameProperty(String value) {
        assertProperty(value, BaseProgressBar::setName, BaseProgressBar::getName);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"value"})
    void unitProperty(String value) {
        assertProperty(value, BaseProgressBar::setUnit, BaseProgressBar::getUnit);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"value"})
    void descriptionProperty(String value) {
        assertProperty(value, BaseProgressBar::setDescription, BaseProgressBar::getDescription);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void hideWhenCompleteProperty(boolean value) {
        assertProperty(value, BaseProgressBar::setHideWhenComplete, BaseProgressBar::isHideWhenComplete);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void removeWhenCompleteProperty(boolean value) {
        assertProperty(value, BaseProgressBar::setRemoveWhenComplete, BaseProgressBar::isRemoveWhenComplete);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void showPercentageProperty(boolean value) {
        assertProperty(value, BaseProgressBar::setShowPercentage, BaseProgressBar::isShowPercentage);
    }

    private <V> void assertProperty(V value, BiConsumer<T, V> setter, Function<T, V> getter) {
        var tested = getProgressBar();

        setter.accept(tested, value);
        assertThat(getter.apply(tested)).isEqualTo(value);
    }
}
