package com.example.clientKTPM.model ;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponse extends TournamentDto {
    private String organizer;
    private String boardType;
    private String organizingMethod;
    private Boolean showButtonDelete;
    private Boolean showButtonUpdate;
    private Boolean showKickOff;
    private Boolean showJoinButton;
}