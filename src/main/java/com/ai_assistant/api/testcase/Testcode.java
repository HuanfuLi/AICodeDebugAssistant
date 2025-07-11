//python
def twoSum(nums, target):
    has = {}
    for i in range(len(nums)):
        if i > 0 and (target - nums[i]) in has:
            return [has[target - nums[i]], i]
        has[nums[i]] = i
    return [0]
