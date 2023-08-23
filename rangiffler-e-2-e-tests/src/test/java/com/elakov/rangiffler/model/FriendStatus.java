package com.elakov.rangiffler.model;

public enum FriendStatus {
  NOT_FRIEND("Invitation sent"), INVITATION_SENT("Invitation sent"), INVITATION_RECEIVED("Invitation sent"), FRIEND("Invitation sent");

  public final String text;

  FriendStatus(String text) {
    this.text = text;
  }
}
