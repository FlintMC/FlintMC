package net.flintmc.mcapi.server.status.pending;

import net.flintmc.mcapi.server.status.ServerStatus;

/** The state of a {@link PendingStatusRequest}. */
public enum PendingStatusState {

  /**
   * The first state of a request, this state will be active before the {@link
   * PendingStatusRequest#start()} has been called.
   */
  IDLE,
  /**
   * This state will be active when {@link PendingStatusRequest#start()} has been called, but not
   * successfully connected.
   */
  CONNECTING,
  /**
   * This state will be active when {@link PendingStatusRequest#start()} has successfully connected
   * to the server.
   */
  RECEIVING,
  /**
   * This state will be active to retrieve the ping between the client and the server when the
   * server has sent the status.
   */
  PINGING,
  /**
   * This state will be active when the server has sent the result to the ping and before the future
   * of the request has been completed.
   */
  FINISHING,
  /**
   * This state will be active when the server has sent an invalid status. The future has been
   * completed with {@code null}.
   */
  FAILED,
  /**
   * This state will be active when the server has sent a valid status and the response to the ping.
   * The future has been completed with the new {@link ServerStatus}.
   */
  FINISHED
}
