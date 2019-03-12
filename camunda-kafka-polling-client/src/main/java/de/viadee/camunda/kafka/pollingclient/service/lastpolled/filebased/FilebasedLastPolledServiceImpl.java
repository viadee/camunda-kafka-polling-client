package de.viadee.camunda.kafka.pollingclient.service.lastpolled.filebased;

import de.viadee.camunda.kafka.pollingclient.config.properties.PollingProperties;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.LastPolledService;
import de.viadee.camunda.kafka.pollingclient.service.lastpolled.PollingTimeslice;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.join;

/**
 * Implementation of file based last polled data.
 * <p>
 * This implementation might be optimized to perform some kind of caching instead of always performing synchronous IO.
 *
 * @author viadee
 * @version $Id: $Id
 */
public class FilebasedLastPolledServiceImpl implements LastPolledService {

    private static final String CUTOFF_TIMESTAMP_PROPERTY = "cutoff";

    private static final String LAST_POLLED_TIMESTAMP_PROPERTY = "lastPolled";

    private final PollingProperties pollingProperties;

    /**
     * <p>
     * Constructor for FilebasedLastPolledServiceImpl.
     * </p>
     *
     * @param pollingProperties
     *            a {@link de.viadee.camunda.kafka.pollingclient.config.properties.PollingProperties} object.
     */
    public FilebasedLastPolledServiceImpl(final PollingProperties pollingProperties) {
        this.pollingProperties = pollingProperties;
    }

    /** {@inheritDoc} */
    @Override
    public PollingTimeslice getPollingTimeslice() {
        final Properties properties = readProperties();
        final Long lastPolledTimestampLong = MapUtils.getLong(properties, LAST_POLLED_TIMESTAMP_PROPERTY);
        final Long cutoffTimestampLong = MapUtils.getLong(properties, CUTOFF_TIMESTAMP_PROPERTY);

        final Date cutoffTimestamp;
        if (cutoffTimestampLong == null) {
            cutoffTimestamp = pollingProperties.getInitialTimestamp();
        } else {
            cutoffTimestamp = new Date(cutoffTimestampLong);
        }

        Date startTimestamp;
        if (lastPolledTimestampLong == null) {
            startTimestamp = cutoffTimestamp;
        } else {
            startTimestamp = new Date(lastPolledTimestampLong);

            if (startTimestamp.compareTo(cutoffTimestamp) < 0) {
                startTimestamp = cutoffTimestamp;
            }
        }

        Date endTimestamp = new Date();
        if (endTimestamp.compareTo(startTimestamp) < 0) {
            endTimestamp = startTimestamp;
        }

        return new PollingTimeslice(cutoffTimestamp, startTimestamp, endTimestamp);
    }

    private Properties readProperties() {
        final File lastPolledFile = pollingProperties.getLastPolledFile();
        final Properties properties = new Properties();

        if (!lastPolledFile.canRead()) {
            return properties;
        }

        try (final BufferedInputStream in = new BufferedInputStream(FileUtils.openInputStream(lastPolledFile))) {
            properties.load(in);
        } catch (final IOException e) {
            throw new RuntimeException(join("Error reading last polled data from ", lastPolledFile), e);
        }

        return properties;
    }

    /** {@inheritDoc} */
    @Override
    public void updatePollingTimeslice(final PollingTimeslice pollingTimeslice) {
        final File lastPolledFile = pollingProperties.getLastPolledFile();

        final Properties properties = new Properties();
        properties.put(LAST_POLLED_TIMESTAMP_PROPERTY, Long.toString(pollingTimeslice.getEndTime().getTime()));
        properties.put(CUTOFF_TIMESTAMP_PROPERTY, Long.toString(pollingTimeslice.getCutoffTime().getTime()));

        try (final BufferedOutputStream out = new BufferedOutputStream(FileUtils.openOutputStream(lastPolledFile))) {
            properties.store(out, "");
        } catch (final IOException e) {
            throw new RuntimeException(join("Error writing last polled timestamp to ", lastPolledFile), e);
        }
    }

}
