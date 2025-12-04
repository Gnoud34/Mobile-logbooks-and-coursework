import 'package:flutter/material.dart';
import '../db/database_helper.dart';
import '../models/hike.dart';
import '../models/observation.dart';
import 'add_hike_screen.dart';
import 'add_observation.dart';

class HikeDetailScreen extends StatefulWidget {
  final Hike hike;
  const HikeDetailScreen({super.key, required this.hike});

  @override
  State<HikeDetailScreen> createState() => _HikeDetailScreenState();
}

class _HikeDetailScreenState extends State<HikeDetailScreen> {
  List<Observation> _obs = [];

  @override
  void initState() {
    super.initState();
    _loadObs();
  }

  Future<void> _loadObs() async {
    final rows = await DatabaseHelper.instance.query(
      'observations',
      where: 'hikeId=?',
      whereArgs: [widget.hike.id],
      orderBy: 'time DESC',
    );
    setState(()=>_obs = rows.map(Observation.fromMap).toList());
  }

  Future<void> _deleteObs(int id) async {
    await DatabaseHelper.instance.deleteById('observations', id);
    _loadObs();
  }

  @override
  Widget build(BuildContext context) {
    final h = widget.hike;
    return Scaffold(
      appBar: AppBar(
        title: Text(h.name),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: () async {
              await Navigator.push(context,
                  MaterialPageRoute(builder: (_) => AddHikeScreen(initial: h)));
              if (!mounted) return;
              Navigator.pop(context); // quay về list, để refresh
            },
          )
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          await Navigator.push(context,
              MaterialPageRoute(builder: (_) => AddObservationScreen(hikeId: h.id!)));
          _loadObs();
        },
        icon: const Icon(Icons.add),
        label: const Text('Add Observation'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('${h.location} • ${h.date} • Parking: ${h.parking}'),
            Text('Length: ${h.length} • Difficulty: ${h.difficulty}'),
            if ((h.description ?? '').isNotEmpty) Text('Desc: ${h.description}'),
            if ((h.weather ?? '').isNotEmpty) Text('Weather: ${h.weather}'),
            if ((h.terrain ?? '').isNotEmpty) Text('Companions: ${h.terrain}'),
            const Divider(height: 24),
            const Text('Observations', style: TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Expanded(
              child: _obs.isEmpty
                  ? const Center(child: Text('No observations yet.'))
                  : ListView.separated(
                itemCount: _obs.length,
                separatorBuilder: (_, __) => const Divider(height:1),
                itemBuilder: (_, i){
                  final o = _obs[i];
                  return ListTile(
                    title: Text(o.observation),
                    subtitle: Text('${o.time}${(o.comment??'').isNotEmpty ? ' • ${o.comment}' : ''}'),
                    trailing: IconButton(
                      icon: const Icon(Icons.delete),
                      onPressed: ()=>_deleteObs(o.id!),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
