class Observation {
  int? id;
  int hikeId;
  String observation, time;
  String? comment;

  Observation({
    this.id,
    required this.hikeId,
    required this.observation,
    required this.time,
    this.comment,
  });

  Map<String, dynamic> toMap() => {
    'id': id,
    'hikeId': hikeId,
    'observation': observation,
    'time': time,
    'comment': comment,
  };

  static Observation fromMap(Map<String, dynamic> m) => Observation(
    id: m['id'],
    hikeId: m['hikeId'],
    observation: m['observation'],
    time: m['time'],
    comment: m['comment'],
  );
}
