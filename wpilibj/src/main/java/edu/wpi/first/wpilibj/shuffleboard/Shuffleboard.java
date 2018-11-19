/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.shuffleboard;

import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The Shuffleboard class provides a mechanism with which data can be added and laid out in the
 * Shuffleboard dashboard application from a robot program. Tabs and layouts can be specified, as
 * well as choosing which widgets to display with and setting properties of these widgets; for
 * example, programmers can specify a specific {@code boolean} value to be displayed with a toggle
 * button instead of the default colored box, or set custom colors for that box.
 *
 * <p>For example, displaying a boolean entry with a toggle button:
 * <pre>{@code
 * NetworkTableEntry myBoolean = Shuffleboard.getTab("Example Tab")
 *   .add("My Boolean", false)
 *   .withWidget("Toggle Button")
 *   .getEntry();
 * }</pre>
 *
 * Changing the colors of the boolean box:
 * <pre>{@code
 * NetworkTableEntry myBoolean = Shuffleboard.getTab("Example Tab")
 *   .add("My Boolean", false)
 *   .withWidget("Boolean Box")
 *   .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "maroon"))
 *   .getEntry();
 * }</pre>
 *
 * Specifying a parent layout. Note that the layout type must <i>always</i> be specified, even if
 * the layout has already been generated by a previously defined entry.
 * <pre>{@code
 * NetworkTableEntry myBoolean = Shuffleboard.getTab("Example Tab")
 *   .getLayout("List", "Example List")
 *   .add("My Boolean", false)
 *   .withWidget("Toggle Button")
 *   .getEntry();
 * }</pre>
 * </p>
 *
 * <p>Teams are encouraged to set up shuffleboard layouts at the start of the robot program.</p>
 */
public final class Shuffleboard {
  /**
   * The name of the base NetworkTable into which all Shuffleboard data will be added.
   */
  public static final String kBaseTableName = "/Shuffleboard";

  private static final ShuffleboardRoot root =
      new ShuffleboardInstance(NetworkTableInstance.getDefault());
  private static final RecordingController recordingController =
      new RecordingController(NetworkTableInstance.getDefault());

  // TODO usage reporting

  private Shuffleboard() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Updates all the values in Shuffleboard. Iterative and timed robots are pre-configured to call
   * this method in the main robot loop; teams using custom robot base classes, or subclass
   * SampleRobot, should make sure to call this repeatedly to keep data on the dashboard up to date.
   */
  public static void update() {
    root.update();
  }

  /**
   * Gets the Shuffleboard tab with the given title, creating it if it does not already exist.
   *
   * @param title the title of the tab
   * @return the tab with the given title
   */
  public static ShuffleboardTab getTab(String title) {
    return root.getTab(title);
  }

  /**
   * Enables user control of widgets containing actuators: speed controllers, relays, etc. This
   * should only be used when the robot is in test mode. IterativeRobotBase and SampleRobot are
   * both configured to call this method when entering test mode; most users should not need to use
   * this method directly.
   */
  public static void enableActuatorWidgets() {
    root.enableActuatorWidgets();
  }

  /**
   * Disables user control of widgets containing actuators. For safety reasons, actuators should
   * only be controlled while in test mode. IterativeRobotBase and SampleRobot are both configured
   * to call this method when exiting in test mode; most users should not need to use
   * this method directly.
   */
  public static void disableActuatorWidgets() {
    update(); // Need to update to make sure the sendable builders are initialized
    root.disableActuatorWidgets();
  }

  /**
   * Starts data recording on the dashboard. Has no effect if recording is already in progress.
   *
   * @see #stopRecording()
   */
  public static void startRecording() {
    recordingController.startRecording();
  }

  /**
   * Stops data recording on the dashboard. Has no effect if no recording is in progress.
   *
   * @see #startRecording()
   */
  public static void stopRecording() {
    recordingController.stopRecording();
  }

  /**
   * Sets the file name format for new recording files to use. If recording is in progress when this
   * method is called, it will continue to use the same file. New recordings will use the format.
   *
   * <p>To avoid recording files overwriting each other, make sure to use unique recording file
   * names. File name formats accept templates for inserting the date and time when the recording
   * started with the {@code ${date}} and {@code ${time}} templates, respectively. For example,
   * the default format is {@code "recording-${time}"} and recording files created with it will have
   * names like {@code "recording-2018.01.15.sbr"}. Users are <strong>strongly</strong> recommended
   * to use the {@code ${time}} template to ensure unique file names.
   * </p>
   *
   * @param format the format for the
   * @see #clearRecordingFileNameFormat()
   */
  public static void setRecordingFileNameFormat(String format) {
    recordingController.setRecordingFileNameFormat(format);
  }

  /**
   * Clears the custom name format for recording files. New recordings will use the default format.
   *
   * @see #setRecordingFileNameFormat(String)
   */
  public static void clearRecordingFileNameFormat() {
    recordingController.clearRecordingFileNameFormat();
  }

  /**
   * Notifies Shuffleboard of an event. Events can range from as trivial as a change in a command
   * state to as critical as a total power loss or component failure. If Shuffleboard is recording,
   * the event will also be recorded.
   *
   * <p>If {@code name} is {@code null} or empty, or {@code importance} is {@code null}, then
   * no event will be sent and an error will be printed to the driver station.
   *
   * @param name        the name of the event
   * @param description a description of the event
   * @param importance  the importance of the event
   */
  public static void addEventMarker(String name, String description, EventImportance importance) {
    recordingController.addEventMarker(name, description, importance);
  }

  /**
   * Notifies Shuffleboard of an event. Events can range from as trivial as a change in a command
   * state to as critical as a total power loss or component failure. If Shuffleboard is recording,
   * the event will also be recorded.
   *
   * <p>If {@code name} is {@code null} or empty, or {@code importance} is {@code null}, then
   * no event will be sent and an error will be printed to the driver station.
   *
   * @param name        the name of the event
   * @param importance  the importance of the event
   */
  public static void addEventMarker(String name, EventImportance importance) {
    addEventMarker(name, "", importance);
  }
}
