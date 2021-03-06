/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.clouddrive.features;

import org.exoplatform.container.component.BaseComponentPlugin;

/**
 * Basic class for Feature API specifications.<br>
 * 
 * See also <a href='http://en.wikipedia.org/wiki/Specification_pattern'>Specification pattern</a>.<br>
 * Created by The eXo Platform SAS.<br>
 * 
 * @author <a href="mailto:pnedonosko@exoplatform.com">Peter Nedonosko</a>
 * @version $Id: FeatureSpec.java 00000 Jan 29, 2014 pnedonosko $
 * 
 */
public abstract class FeatureSpec<T> extends BaseComponentPlugin {

  static abstract class CompositeSpec<C> extends FeatureSpec<C> {
    final FeatureSpec<C> one;

    final FeatureSpec<C> other;

    public CompositeSpec(FeatureSpec<C> one, FeatureSpec<C> other) {
      this.one = one;
      this.other = other;
    }
  }

  static class AndSpec<C> extends CompositeSpec<C> {
    AndSpec(FeatureSpec<C> one, FeatureSpec<C> other) {
      super(one, other);
    }

    public boolean isSatisfiedBy(C target) {
      return one.isSatisfiedBy(target) && other.isSatisfiedBy(target);
    }
  }

  static class OrSpec<C> extends CompositeSpec<C> {
    OrSpec(FeatureSpec<C> one, FeatureSpec<C> other) {
      super(one, other);
    }

    public boolean isSatisfiedBy(C target) {
      return one.isSatisfiedBy(target) || other.isSatisfiedBy(target);
    }
  }

  class NotSpec<C> extends FeatureSpec<C> {
    final FeatureSpec<C> one;

    NotSpec(FeatureSpec<C> one) {
      this.one = one;
    }

    public boolean isSatisfiedBy(C target) {
      return !one.isSatisfiedBy(target);
    }
  }

  /**
   * Empty constructor.
   */
  public FeatureSpec() {
  }

  /**
   * Whether given target satisfies current specification.
   * 
   * @param target
   * @return
   */
  public abstract boolean isSatisfiedBy(T target);

  /**
   * Cascade this and other spec in logical AND specification. If both will satisfy then only this will does
   * also.
   * 
   * @param other FeatureSpec
   * @return FeatureSpec
   */
  public final FeatureSpec<T> and(FeatureSpec<T> other) {
    return new AndSpec<T>(this, other);
  }

  /**
   * Cascade this and other spec in logical OR specification. If any of both will satisfy then only this will
   * does also.
   * 
   * @param other FeatureSpec
   * @return FeatureSpec
   */
  public final FeatureSpec<T> or(FeatureSpec<T> other) {
    return new OrSpec<T>(this, other);
  }

  /**
   * Create a specification opposite to this one. The new spec will satisfy only if this one will not.
   * 
   * @return FeatureSpec
   */
  public final FeatureSpec<T> not() {
    return new NotSpec<T>(this);
  }

}
