class Hike {
  int? id;
  String name, location, date, difficulty, parking, length;
  String? description, weather, terrain;

  Hike ({
   this.id,
   required this.name,
   required this.location,
   required this.date,
   required this.difficulty,
   required this.parking,
   required this.length,

   this.description,
   this.terrain,
   this.weather,
});


  Map<String, dynamic> toMap() => {
    'id': id,
    'name': name,
    'location': location,
    'date': date,
    'difficulty': difficulty,
    'parking': parking,
    'length' : length,

    'description': description,
    'weather' :  weather,
    'terrain' : terrain,
  };

  static Hike fromMap(Map<String, dynamic> m ) => Hike(
  id: m['id'],
  name: m['name'],
  location: m['location'],
  date: m['date'],
  difficulty: m['difficulty'],
  parking: m['parking'],
  length: m['length'],

  description: m['description'],
  weather: m['weather'],
  terrain: m['terrain'] ,
  );

}