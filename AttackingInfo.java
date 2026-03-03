import java.util.Set;

public class AttackingInfo {
    Set<Positon> whiteAttacking;
    Set<Positon> blackAttacking;

    AttackingInfo(Set<Positon> whiteAttacking, Set<Positon> blackAttacking) {
        this.whiteAttacking = whiteAttacking;
        this.blackAttacking = blackAttacking;
    }
}
