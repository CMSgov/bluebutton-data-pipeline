package gov.hhs.cms.bluebutton.data.pipeline.rif.load;

import java.util.Optional;

import gov.hhs.cms.bluebutton.data.model.rif.RifRecordEvent;

/**
 * This unchecked {@link RuntimeException} is used to represent that one or more
 * {@link RifRecordEvent}s failed to load, when pushed to a FHIR server via
 * {@link RifLoader}.
 */
public final class RifLoadFailure extends RuntimeException {
	private static final long serialVersionUID = 5268467019558996698L;

	private static final boolean LOG_SOURCE_DATA = false;

	private final RifRecordEvent<?> failedRecordEvent;

	/**
	 * Constructs a new {@link RifLoadFailure} instance, for a specific
	 * {@link RifRecordEvent} failure.
	 * 
	 * @param failedRecordEvent
	 *            the value to use for {@link #getFailedRecordEvent()}
	 * @param cause
	 *            the {@link Throwable} that was encountered, when the
	 *            {@link RifRecordEvent} failed to load
	 */
	public RifLoadFailure(RifRecordEvent<?> failedRecordEvent, Throwable cause) {
		super(buildMessage(failedRecordEvent), cause);
		this.failedRecordEvent = failedRecordEvent;
	}

	/**
	 * Constructs a new {@link RifLoadFailure} instance, for a more general
	 * failure to load one of more {@link RifRecordEvent}s.
	 * 
	 * @param cause
	 *            the {@link Throwable} that was encountered, when the
	 *            {@link RifRecordEvent}(s) failed to load
	 */
	public RifLoadFailure(Throwable cause) {
		super(cause);
		this.failedRecordEvent = null;
	}

	/**
	 * @param inputBundle
	 *            the {@link TransformedBundle} that failed to load
	 * @return the value to use for {@link #getMessage()}
	 */
	private static String buildMessage(RifRecordEvent<?> failedRecordEvent) {
		if (LOG_SOURCE_DATA)
			return String.format("Failed to load a '%s' record: '%s'.",
					failedRecordEvent.getFile().getFileType().name(), failedRecordEvent.toString());
		else
			return String.format("Failed to load a '%s' record.", failedRecordEvent.getFile().getFileType().name());
	}

	/**
	 * @return the {@link RifRecordEvent} that failed to load, if known
	 */
	public Optional<RifRecordEvent<?>> getFailedRecordEvent() {
		return Optional.ofNullable(failedRecordEvent);
	}
}