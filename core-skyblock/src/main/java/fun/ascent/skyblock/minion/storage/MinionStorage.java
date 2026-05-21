package fun.ascent.skyblock.minion.storage;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public final class MinionStorage {
    private final List<ItemStack> stacks = new ArrayList<>();

    public boolean canFit(List<ItemStack> incoming, int maxSlots) {
        List<ItemStack> preview = snapshot();
        for (ItemStack stack : incoming) {
            if (!tryAdd(preview, stack, maxSlots)) {
                return false;
            }
        }
        return true;
    }

    public void addAll(List<ItemStack> incoming, int maxSlots) {
        for (ItemStack stack : incoming) {
            if (!tryAdd(stacks, stack, maxSlots)) {
                return;
            }
        }
    }

    public List<ItemStack> collectAll() {
        if (stacks.isEmpty()) {
            return null;
        }
        List<ItemStack> collected = snapshot();
        stacks.clear();
        return collected;
    }

    public void remove(int index) {
        if (index >= 0 && index < stacks.size()) {
            stacks.remove(index);
        }
    }


    public List<ItemStack> snapshot() {
        List<ItemStack> copy = new ArrayList<>(stacks.size());
        for (ItemStack stack : stacks) {
            copy.add(stack.withAmount(stack.amount()));
        }
        return copy;
    }

    public List<ItemStack> snapshotSlots(int maxSlots) {
        List<ItemStack> copy = snapshot();
        if (copy.size() >= maxSlots) {
            return copy.subList(0, maxSlots);
        }
        return copy;
    }

    public int totalItems() {
        int total = 0;
        for (ItemStack stack : stacks) {
            total += stack.amount();
        }
        return total;
    }

    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    private boolean tryAdd(List<ItemStack> target, ItemStack stack, int maxSlots) {
        if (stack == null || stack.isAir() || stack.amount() <= 0) {
            return true;
        }
        int remaining = stack.amount();
        for (int i = 0; i < target.size(); i++) {
            ItemStack existing = target.get(i);
            if (existing.material() != stack.material() || existing.amount() >= 64) {
                continue;
            }
            int free = 64 - existing.amount();
            int moved = Math.min(free, remaining);
            target.set(i, existing.withAmount(existing.amount() + moved));
            remaining -= moved;
            if (remaining <= 0) {
                return true;
            }
        }
        while (remaining > 0) {
            if (target.size() >= maxSlots) {
                return false;
            }
            int moved = Math.min(64, remaining);
            target.add(stack.withAmount(moved));
            remaining -= moved;
        }
        compress(target);
        return true;
    }

    private void compress(List<ItemStack> target) {
        target.removeIf(stack -> stack == null || stack.material() == Material.AIR || stack.amount() <= 0);
    }
}
