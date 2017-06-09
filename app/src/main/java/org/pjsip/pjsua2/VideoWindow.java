/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class VideoWindow {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected VideoWindow(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(VideoWindow obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_VideoWindow(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VideoWindow(int win_id) {
    this(pjsua2JNI.new_VideoWindow(win_id), true);
  }

  public VideoWindowInfo getInfo() throws Exception {
    return new VideoWindowInfo(pjsua2JNI.VideoWindow_getInfo(swigCPtr, this), true);
  }

  public void Show(boolean show) throws Exception {
    pjsua2JNI.VideoWindow_Show(swigCPtr, this, show);
  }

  public void setPos(MediaCoordinate pos) throws Exception {
    pjsua2JNI.VideoWindow_setPos(swigCPtr, this, MediaCoordinate.getCPtr(pos), pos);
  }

  public void setSize(MediaSize size) throws Exception {
    pjsua2JNI.VideoWindow_setSize(swigCPtr, this, MediaSize.getCPtr(size), size);
  }

  public void rotate(int angle) throws Exception {
    pjsua2JNI.VideoWindow_rotate(swigCPtr, this, angle);
  }

  public void setWindow(VideoWindowHandle win) throws Exception {
    pjsua2JNI.VideoWindow_setWindow(swigCPtr, this, VideoWindowHandle.getCPtr(win), win);
  }

}
