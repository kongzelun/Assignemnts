#!python

import os
import sys

states = ("H", "C")

#observations = []

initial_probability = {"H": 0.8, "C": 0.2}

transition_probability = {
    'H': {"H": 0.7, "C": 0.3},
    "C": {"H": 0.4, "C": 0.6}
}

observation_likelihoods = {
    "H": {"1": 0.4, "2": 0.3, "3": 0.3},
    "C": {"1": 0.4, "2": 0.4, "3": 0.2}
}


def viterbi(observations):
    # an array with columns corresponding to inputs and rows corresponding to possible states
    V = [{}]
    path = {}

    # t == 0
    for i in states:
        V[0][i] = initial_probability[i] * \
            observation_likelihoods[i][observations[0]]
        path[i] = [i]

    # t > 0.
    # Instead of using recurrence, I use loop!!!
    for t in range(1, len(observations)):
        V.append({})
        newpath = {}

        for i in states:
            (probability, state) = max((V[t - 1][y0] * transition_probability[y0]
                                        [i] * observation_likelihoods[i][observations[t]], y0) for y0 in states)
            V[t][i] = probability
            newpath[i] = path[state] + [i]

        path = newpath

    (probability, states_chain) = max((V[t][i], i) for i in states)
    return (probability, path[state])


def main():
    observations = []
    if len(sys.argv) > 1:
        for c in sys.argv[1]:
            observations.append(c)
        print(viterbi(observations))
    else:
        print("Please input the observations!")


if __name__ == "__main__":
    main()
