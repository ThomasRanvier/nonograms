import sys

def calc_perms(r, cur, spaces, perm, shift, res):
    global col_val, col_ix, mask, val, grid, row_perms
    if cur == len(row_rules[r]):
        if (grid[r] & perm) == grid[r]:
            res.append(perm)
    else:
        while spaces >= 0:
            calc_perms(r, cur + 1, spaces, perm | (((1 << row_rules[r][cur]) - 1) << shift), shift + row_rules[r][cur] + 1, res);
            shift += 1;
            spaces -= 1;

def update_cols(row):
    global col_val, col_ix, mask, val, grid, row_perms
    ixc = 1
    for c in range(cols):
        col_val[row][c] = 0 if row == 0 else col_val[row - 1][c]
        col_ix[row][c] = 0 if row == 0 else col_ix[row - 1][c]
        if (grid[row] & ixc) == 0:
            if row > 0 and col_val[row - 1][c] > 0:
                col_val[row][c] = 0
                col_ix[row][c] += 1
        else:
            col_val[row][c] += 1
        ixc <<= 1

def row_mask(row):
    global col_val, col_ix, mask, val, grid, row_perms
    mask[row] = 0
    val[row] = 0
    if row == 0:
        return
    ixc = 1
    for c in range(cols):
        if col_val[row - 1][c] > 0:
            mask[row] |= ixc
            print('')
            print(col_rules[c])
            print(col_ix[row - 1][c])
            if col_rules[c][col_ix[row - 1][c]] > col_val[row - 1][c]:
                val[row] |= ixc
        elif col_val[row - 1][c] == 0 and col_ix[row - 1][c] == len(col_rules[c]):
            mask[row] |= ixc
        ixc <<= 1

def dfs(row):
    global col_val, col_ix, mask, val, grid, row_perms
    if row == rows:
        return True
    row_mask(row)
    for i in range(len(row_perms[row])):
        if (row_perms[row][i] & mask[row]) != val[row]:
            continue
        grid[row] = row_perms[row][i]
        update_cols(row)
        if dfs(row + 1):
            return True
    return False

def display():
    global col_val, col_ix, mask, val, grid, row_perms
    for r in range(rows):
        for c in range(cols):
            print(' ' if (grid[r] & (1 << c)) == 0 else '#', end='')
        print()

def main():
    global col_val, col_ix, mask, val, grid, row_perms
    for r in range(rows):
        row_rules[r] = [row_rules[r][i] for i in range(row_rule_len) if row_rules[r][i] > 0]
    for c in range(cols):
        col_rules[c] = [col_rules[c][i] for i in range(col_rule_len) if col_rules[c][i] > 0]
    grid = [0] * rows
    row_perms = [0] * rows
    for r in range(rows):
        res = []
        spaces = cols - (len(row_rules[r]) - 1)
        for i in range(len(row_rules[r])):
            spaces -= row_rules[r][i]
        calc_perms(r, 0, spaces, 0, 0, res)
        if len(res) <= 0:
            return False
        row_perms[r] = [0] * len(res)
        while len(res) > 0:
            row_perms[r][len(res) - 1] = res.pop()
    col_val = [[0] * cols] * rows
    col_ix = [[0] * cols] * rows
    mask = [0] * rows
    val = [0] * rows
    if dfs(0):
        print('success')
        display(grid)
    else:
        print('failure')

col_val = []
col_ix = []
mask = []
val = []
grid = []
row_perms = []

#
# Default problem
#
# From http://twan.home.fmf.nl/blog/haskell/Nonograms.details
# The lambda picture
#
rows = 12
row_rule_len = 3
row_rules = [
    [0, 0, 2],
    [0, 1, 2],
    [0, 1, 1],
    [0, 0, 2],
    [0, 0, 1],
    [0, 0, 3],
    [0, 0, 3],
    [0, 2, 2],
    [0, 2, 1],
    [2, 2, 1],
    [0, 2, 3],
    [0, 2, 2]
]

cols = 10
col_rule_len = 2
col_rules = [
    [2, 1],
    [1, 3],
    [2, 4],
    [3, 4],
    [0, 4],
    [0, 3],
    [0, 3],
    [0, 3],
    [0, 2],
    [0, 2]
]

if __name__ == '__main__':
    if len(sys.argv) > 1:
        file = sys.argv[1]
        exec(compile(open(file).read(), file, 'exec'))
    main()
