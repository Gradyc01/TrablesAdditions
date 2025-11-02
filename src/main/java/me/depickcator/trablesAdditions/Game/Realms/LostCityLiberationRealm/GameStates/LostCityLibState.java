package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.AbstractRealmStates;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Objective;

import java.util.List;
import java.util.Set;

public abstract class LostCityLibState extends AbstractRealmStates {
    private final LostCityLiberationRealm realm;
    public LostCityLibState(LostCityLiberationRealm realm) {
        super(realm);
        this.realm = realm;
    }

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
        Component indent = TextUtil.makeText(" ");
        maker.editLine(board, 10, indent.append(TextUtil.makeText("Objective: ")));
        List<Component> objective = getObjectiveName();
        maker.editLine(board, 9, indent.append(objective.getFirst()));
        maker.editLine(board, 8, indent.append(objective.size() >= 2 ? objective.get(1) : TextUtil.makeText("")));
        super.update(maker, board, playerData);
    }

    /*Returns a List of Components that describe the objective name
     * Only the first two in the list will be used*/
    public abstract List<Component> getObjectiveName();

    @Override
    public String observerName() {
        return "Lost City: Liberation Realm";
    }

    public LostCityLiberationRealm getRealm() {
        return realm;
    }
}
