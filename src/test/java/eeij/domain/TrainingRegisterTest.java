package eeij.domain;

import static org.assertj.core.api.Assertions.assertThat;

import eeij.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingRegisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingRegister.class);
        TrainingRegister trainingRegister1 = new TrainingRegister();
        trainingRegister1.setId(1L);
        TrainingRegister trainingRegister2 = new TrainingRegister();
        trainingRegister2.setId(trainingRegister1.getId());
        assertThat(trainingRegister1).isEqualTo(trainingRegister2);
        trainingRegister2.setId(2L);
        assertThat(trainingRegister1).isNotEqualTo(trainingRegister2);
        trainingRegister1.setId(null);
        assertThat(trainingRegister1).isNotEqualTo(trainingRegister2);
    }
}
