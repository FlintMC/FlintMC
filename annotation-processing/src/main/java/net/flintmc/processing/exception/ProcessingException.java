/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.processing.exception;

import javax.lang.model.element.Element;

/**
 * Represents a general, fatal failure while processing. This should only be used for cases such as
 * failing to write a file due to an {@link java.io.IOException} or similar, whereas invalid
 * annotations should be signaled as errored via the current {@link javax.annotation.processing.Messager}
 */
public class ProcessingException extends RuntimeException {

  private final Element sourceElement;

  /**
   * Creates a new {@link ProcessingException} with the specified message. The {@link Element}
   * causing this exception is unknown.
   *
   * @param message The message to pass to the {@link RuntimeException}
   */
  public ProcessingException(String message) {
    super(message);
    this.sourceElement = null;
  }

  /**
   * Creates a new {@link ProcessingException} with the specified message and cause. The {@link
   * Element} causing this exception is unknown.
   *
   * @param message The message to pass to the {@link RuntimeException}
   * @param cause   The cause to pass to the {@link RuntimeException}
   */
  public ProcessingException(String message, Throwable cause) {
    super(message, cause);
    this.sourceElement = null;
  }

  /**
   * Creates a new {@link ProcessingException} with the specified message and an element indicating
   * the source of the exception.
   *
   * @param message       The message to pass to the {@link RuntimeException}
   * @param sourceElement The element during which processing the exception occurred
   */
  public ProcessingException(String message, Element sourceElement) {
    super(message);
    this.sourceElement = sourceElement;
  }

  /**
   * Creates a new {@link ProcessingException} with the specified message, a cause and an element
   * indicating the source of the exception.
   *
   * @param message       The message to pass to the {@link RuntimeException}
   * @param cause         The cause to pass to the {@link RuntimeException}
   * @param sourceElement The element during which processing the exception occurred
   */
  public ProcessingException(String message, Throwable cause, Element sourceElement) {
    super(message, cause);
    this.sourceElement = sourceElement;
  }

  /**
   * Retrieves the element during which processing the exception occurred.
   *
   * @return The element which processing caused the exception, or null if not caused by handling of
   * an element
   */
  public Element getSourceElement() {
    return sourceElement;
  }
}
