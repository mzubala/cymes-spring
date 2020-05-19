package pl.com.bottega.cymes.showscheduler.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.com.bottega.cymes.showscheduler.adapters.SuspensionCheckerAdapter;
import pl.com.bottega.cymes.showscheduler.domain.ShowExample;

import javax.inject.Inject;
import javax.ws.rs.ServerErrorException;
import java.util.Random;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(Arquillian.class)
public class SuspensionCheckerAdapterTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule(options().port(8888).httpsPort(8889));

    @Inject
    private SuspensionCheckerAdapter suspensionCheckerAdapter;

    @Deployment
    public static Archive<?> createTestArchive() {
        return DeploymentFactory.createTestArchive();
    }

    private CinemaAbility cinemaAbility;

    @Before
    public void setup() {
        cinemaAbility = new CinemaAbility(wireMock);
    }

    @Test
    public void returnsFalseWhenNeitherCinemaNotCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        cinemaAbility.cinemaSuspensionCheckReturns(show, false);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, false);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void returnsTrueWhenEitherCinemaOrCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        var randBool = new Random().nextDouble() > 0.5;
        cinemaAbility.cinemaSuspensionCheckReturns(show, randBool);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, !randBool);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void returnsTrueWhenBothCinemaAndCinemaHallAreSuspended() {
        // given
        var show = new ShowExample().toShow();
        cinemaAbility.cinemaSuspensionCheckReturns(show, true);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, true);

        // when
        var result = suspensionCheckerAdapter.anySuspensionsAtTimeOf(show);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void throwsExceptionWhenOneOfTheDependenciesFails() {
        // given
        var show = new ShowExample().toShow();
        cinemaAbility.cinemaSuspensionCheckReturnsError(show);
        cinemaAbility.cinemaHallSuspensionCheckReturns(show, true);

        // then
        assertThatThrownBy(() -> suspensionCheckerAdapter.anySuspensionsAtTimeOf(show)).hasCauseInstanceOf(ServerErrorException.class);
    }
}
